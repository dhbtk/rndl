import * as React from 'react';
import { LoginState } from '../../api/services/LoginService';

export interface Props {
    loginState: LoginState;
}

export interface State {
    email: string;
    password: string;
    loading: boolean;
}

export default class LogInRoute extends React.Component<Props, State> {
    constructor(props: Props) {
        super(props);
        this.state = {
            email: '',
            password: '',
            loading: false
        };
    }

    render() {
        return (
            <span>Login</span>
        );
    }
}