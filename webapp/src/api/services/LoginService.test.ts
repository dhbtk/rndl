import { authHeaders, getLoginState, logIn, logOut, setLoginState, subscribe, unsubscribe } from './LoginService';
import Mock = jest.Mock;

describe('getLoginState', () => {
    it('returns empty state on startup', () => {
        const { token, expirationDate, user } = getLoginState();
        expect(token).toBe(null);
        expect(expirationDate).toBe(null);
        expect(user).toBe(null);
    });
});

describe('subscribe', () => {
    it('returns initial state on subscribe', () => {
        const mockCallback = jest.fn();
        subscribe(mockCallback);
        expect(mockCallback.mock.calls.length).toBe(1);
        setLoginState({ ...getLoginState(), token: 'abc' });
        expect(mockCallback.mock.calls.length).toBe(2);
        expect(mockCallback.mock.calls[1][0]).toHaveProperty('token', 'abc');
    });
});

describe('unsubscribe', () => {
    it('allows subscribers to unsubscribe', () => {
        const mockCallback = jest.fn();
        const sym = subscribe(mockCallback);
        expect(mockCallback.mock.calls.length).toBe(1);
        unsubscribe(sym);
        setLoginState({ ...getLoginState(), token: 'abc' });
        expect(mockCallback.mock.calls.length).toBe(1);
    });
});

describe('authHeaders', () => {
    it('sets correct headers', () => {
        setLoginState({ ...getLoginState(), token: 'TOKEN' });
        expect(authHeaders()).toHaveProperty('headers.Authorization', 'Bearer TOKEN');
    });
    it('does not overwrite other headers', () => {
        setLoginState({ ...getLoginState(), token: 'TOKEN' });
        const init: RequestInit = {
            headers: {
                'X-Mock-Header': '123456'
            }
        };
        const newInit = authHeaders(init);
        expect(newInit.headers).toHaveProperty('X-Mock-Header', '123456');
        expect(newInit.headers).toHaveProperty('Authorization', 'Bearer TOKEN');
    });
    it('does not overwrite other properties', () => {
        setLoginState({ ...getLoginState(), token: 'TOKEN' });
        const init: RequestInit = {
            headers: {
                'Content-Type': 'text/plain'
            },
            body: 'plain string'
        };
        const newInit = authHeaders(init);
        expect(newInit.headers).toHaveProperty('Content-Type', 'text/plain');
        expect(newInit.headers).toHaveProperty('Authorization', 'Bearer TOKEN');
        expect(newInit).toHaveProperty('body', 'plain string');
    });
});

const mockLoginFetch = () => {
    const mockFetch = jest.fn();
    mockFetch.mockReturnValueOnce(Promise.resolve({
        json: function() {
            return Promise.resolve({
                access_token: 'MOCKTOKEN',
                expires_in: '1234567890'
            });
        }
    }));
    mockFetch.mockReturnValueOnce(Promise.resolve({
        json: function() {
            return Promise.resolve({
                id: 1,
                fullName: 'TestUser',
                email: 'test@test.com'
            });
        }
    }));
    return mockFetch;
};

describe('login and getting login state', () => {
    it('logs in and returns correct login state', done => {
        window.fetch = mockLoginFetch();
        logIn('abc', '123').then(() => {
            const { token, expirationDate, user } = getLoginState();
            expect(token).toBe('MOCKTOKEN');
            expect(expirationDate).toBeInstanceOf(Date);
            expect(user).toHaveProperty('id', 1);
            expect(user).toHaveProperty('email', 'test@test.com');
            done();
        });
    });
    it('logs in and logs out', done => {
        window.fetch = mockLoginFetch();
        logIn('abc', '123').then(() => {
            const { token, expirationDate, user } = getLoginState();
            expect(token).toBe('MOCKTOKEN');
            expect(expirationDate).toBeInstanceOf(Date);
            expect(user).toHaveProperty('id', 1);
            expect(user).toHaveProperty('email', 'test@test.com');
            (window.fetch as Mock<Promise<null>>).mockReturnValueOnce(Promise.resolve());
            expect(logOut()).resolves.toBeUndefined().then(() => done());
        });
    });
});