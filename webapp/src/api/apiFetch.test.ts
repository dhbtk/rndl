import { apiFetch, authFetch } from './apiFetch';
import { getLoginState, setLoginState } from './services/LoginService';

it('Throws error on non-200 response', done => {
    window.fetch = jest.fn()
        .mockReturnValueOnce(Promise.resolve({ ok: false }));
    expect(apiFetch('/')).rejects.toBeTruthy().then(() => done());
});

it('Returns JSON on a 200 response', done => {
    window.fetch = jest.fn()
        .mockReturnValueOnce(Promise.resolve({
            ok: true,
            json() {
                return Promise.resolve({ object: 'text' });
            }
        }));
    expect(apiFetch('/')).resolves.toHaveProperty('object', 'text').then(() => done());
});

it('correctly applies headers on authFetch', done => {
    setLoginState({ ...getLoginState(), token: 'TOKEN' });
    window.fetch = jest.fn((info, init) => {
        return Promise.resolve({
            ok: true,
            json() {
                return Promise.resolve(init);
            }
        });
    });
    expect(authFetch('/')).resolves.toHaveProperty('headers.Authorization', 'Bearer TOKEN').then(() => done());
});