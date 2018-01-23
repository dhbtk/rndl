import { LoginState } from '../api/services/LoginService';
import { Redirect } from 'react-router';
import * as React from 'react';

export interface Props {
    loginState: LoginState;
}

export default function RootRoute({ loginState }: Props) {
    if (!loginState.user) {
        return <Redirect to="/login"/>;
    } else {
        return <span>root</span>;
    }
}