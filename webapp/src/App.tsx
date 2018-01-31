import * as React from 'react';
import { initialize, LoginState, subscribe, Subscription, unsubscribe } from './api/services/LoginService';
import { BrowserRouter } from 'react-router-dom';
import { Route, Switch } from 'react-router';
import LogInRoute from './components/login/LogInRoute';
import RootRoute from './components/RootRoute';
import Reboot from 'material-ui/Reboot';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import theme from './theme';
import { CircularProgress } from 'material-ui';
import { IntlProvider } from 'react-intl';
import 'react-intl/locale-data/en';
import 'react-intl/locale-data/pt';
import messages from './i18n/messages';

export interface State {
    login: LoginState;
    subscription: Subscription;
    initializing: boolean;
}

class App extends React.Component<{}, State> {
    constructor(props: {}) {
        super(props);
    }

    componentWillMount() {
        this.setState({
            subscription: subscribe((login) => this.setState({ login })),
            initializing: true
        });
        const stop = () => this.setState({ initializing: false });
        initialize().then(stop, stop);
    }

    componentWillUnmount() {
        unsubscribe(this.state.subscription);
    }

    render() {
        if (this.state.initializing) {
            return (
                <MuiThemeProvider theme={theme}>
                    <Reboot/>
                    <div
                        style={{
                            width: '100vw',
                            height: '100vh',
                            display: 'flex',
                            justifyContent: 'center',
                            alignItems: 'center'
                        }}
                    >
                        <CircularProgress size={32}/>
                    </div>
                </MuiThemeProvider>
            );
        }
        const loginState = this.state.login;
        return (
            <IntlProvider locale="en" messages={messages.en}>
                <MuiThemeProvider theme={theme}>
                    <div>
                        <Reboot/>
                        <BrowserRouter>
                            <Switch>
                                <Route path="/login" render={() => <LogInRoute loginState={loginState}/>}/>
                                <Route path="/" render={() => <RootRoute loginState={loginState}/>}/>
                            </Switch>
                        </BrowserRouter>
                    </div>
                </MuiThemeProvider>
            </IntlProvider>
        );
    }
}

export default App;
