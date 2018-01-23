import * as React from 'react';
import { initialize, LoginState, subscribe, Subscription, unsubscribe } from './api/services/LoginService';
import { BrowserRouter } from 'react-router-dom';
import { Route, Switch } from 'react-router';
import LogInRoute from './components/login/LogInRoute';
import RootRoute from './components/RootRoute';
import Reboot from 'material-ui/Reboot';

export interface State {
    login: LoginState;
    subscription: Subscription;
}

class App extends React.Component<{}, State> {
    constructor(props: Object) {
        super(props);
    }

    componentWillMount() {
        this.setState({ subscription: subscribe((login) => this.setState({ login })) });
        initialize();
    }

    componentWillUnmount() {
        unsubscribe(this.state.subscription);
    }

    render() {
        const loginState = this.state.login;
        return (
            <div>
                <Reboot/>
                <BrowserRouter>
                    <Switch>
                        <Route path="/login" render={() => <LogInRoute loginState={loginState}/>}/>
                        <Route path="/" render={() => <RootRoute loginState={loginState}/>}/>
                    </Switch>
                </BrowserRouter>
            </div>
        );
    }
}

export default App;
