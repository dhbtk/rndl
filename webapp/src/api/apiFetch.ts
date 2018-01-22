import { authHeaders } from './services/LoginService';

export function apiFetch<T>(info: RequestInfo, init?: RequestInit): Promise<T> {
    return fetch(info, init)
        .then(response => {
            if (!response.ok) {
                throw new Error('A server error ocurred.');
            } else {
                return response.json();
            }
        });
}

export function authFetch<T>(info: RequestInfo, init?: RequestInit): Promise<T> {
    return apiFetch(info, authHeaders(init));
}