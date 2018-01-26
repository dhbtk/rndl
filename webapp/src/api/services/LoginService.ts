import User from '../entities/user/User';
import 'url-search-params-polyfill';

if (window.localStorage === undefined) {
    const localStorageMock = (function() {
        let store = {};

        return {
            getItem: function(key: string) {
                return store[key] || null;
            },
            setItem: function(key: string, value: string) {
                store[key] = value.toString();
            },
            clear: function() {
                store = {};
            }
        };

    })();

    Object.defineProperty(window, 'localStorage', {
        value: localStorageMock
    });
}

//
// Types
//

export interface LoginState {
    token: string | null;
    expirationDate: Date | null;
    user: User | null;
}

export type LoginStateSubscriber = (state: LoginState) => void;

export type Subscription = Symbol;

interface Subscriber {
    key: Subscription;
    value: LoginStateSubscriber;
}

//
// Initialization
//

const storedExpiration = window.localStorage.getItem('expirationDate');

let loginState: LoginState = {
    token: window.localStorage.getItem('token'),
    expirationDate: storedExpiration != null ? new Date(storedExpiration) : null,
    user: null
};
let subscribers: Subscriber[] = [];

//
// Functions
//

/**
 * Returns the current login state.
 */
export const getLoginState = () => loginState;

/**
 *
 * @param {LoginState} state
 */
export const setLoginState = (state: LoginState) => {
    loginState = state;
    subscribers.forEach(({ value }) => value(state));
};

/**
 *
 * @param {LoginStateSubscriber} value
 */
export const subscribe = (value: LoginStateSubscriber): Subscription => {
    const key: Subscription = Symbol();
    subscribers.push({ key, value });
    value(loginState);
    return key;
};

/**
 *
 * @param key
 */
export const unsubscribe = (key: Subscription) => {
    subscribers = subscribers.filter(sub => sub.key !== key);
};

/**
 *
 * @param {RequestInit} existing
 */
export const authHeaders = (existing: RequestInit = {}): RequestInit => ({
    ...existing,
    headers: Object.assign((existing.headers || {}), { 'Authorization': `Bearer ${getLoginState().token}` })
});

/**
 *
 */
export const logIn = (email: string, password: string) => {
    const data = new URLSearchParams();
    data.set('username', email);
    data.set('password', password);
    return fetch('/api/token', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: data
    })
        .then(response => response.ok ? response.json() : Promise.reject(null))
        .then(({ expires_in, access_token }) => {
            return fetch('/api/user', { headers: { 'Authorization': `Bearer ${access_token}` } })
                .then(response => response.json()).then((user: User) => {
                    const expirationDate = new Date(new Date().getMilliseconds() + expires_in);
                    window.localStorage.setItem('token', access_token);
                    window.localStorage.setItem('expirationDate', expirationDate.getMilliseconds().toString());
                    return {
                        token: access_token,
                        expirationDate,
                        user
                    } as LoginState;
                });
        })
        .then(state => setLoginState(state));
};

export const logOut = () => {
    if (getLoginState().token === null) {
        return Promise.reject('Not logged in');
    } else {
        window.localStorage.clear();
        return fetch('/api/token', authHeaders({ method: 'DELETE' }))
            .then(() => setLoginState({ token: null, user: null, expirationDate: null }));
    }
};

//
// Initialization
//

export const initialize = (): Promise<User> => {
    if (loginState.expirationDate != null && loginState.expirationDate > new Date()) {
        setLoginState({ expirationDate: null, token: null, user: null });
        return Promise.reject(null);
    }

    if (loginState.token) {
        return new Promise<User>(((resolve, reject) => {
            fetch('/api/user', authHeaders()).then(request => request.json()).then(user => setLoginState({
                ...loginState,
                user
            }))
                .then(
                    () => resolve(getLoginState().user as User),
                    () => {
                        setLoginState({
                            token: null,
                            expirationDate: null,
                            user: null
                        });
                        reject(null);
                    });
        }));

    } else {
        return Promise.reject(null);
    }
};
