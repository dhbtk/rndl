import * as React from 'react';
import { FormEvent } from 'react';
import { getLoginState, LoginState, signIn } from '../../api/services/LoginService';
import { StyleRules, Theme } from 'material-ui/styles';
import withStyles from 'material-ui/styles/withStyles';
import { Button, CircularProgress, TextField, Typography, WithStyles } from 'material-ui';
import Paper from 'material-ui/Paper';
import { Redirect } from 'react-router';
import { FormattedMessage, InjectedIntlProps, injectIntl } from 'react-intl';

export interface Props {
    loginState: LoginState;
}

export interface State {
    email: string;
    password: string;
    loading: boolean;
    error: boolean;
    loggedIn: boolean;
}

const loginStyle = (theme: Theme): StyleRules => ({
    root: {
        backgroundImage: `url(${require('./background.jpg')})`,
        height: '100vh',
        width: '100vw',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center'
    },
    paper: {
        [theme.breakpoints.up('sm')]: {
            width: '480px',
            padding: '16px'
        },
        [theme.breakpoints.down('sm')]: {
            width: '100%',
            height: '100%',
            padding: '8px'
        }
    },
    tempLogo: {
        textAlign: 'center',
        marginTop: '16px',
        marginBottom: '16px'
    },
    form: {
        display: 'flex',
        flexDirection: 'column',
        padding: '32px',
        '& > *:not(:last-child)': {
            marginBottom: '16px'
        }
    }
});

type InternalProps = Props & WithStyles<string> & InjectedIntlProps;

class LogInRoute extends React.Component<InternalProps, State> {
    constructor(props: InternalProps) {
        super(props);
        this.state = {
            email: '',
            password: '',
            loading: false,
            error: false,
            loggedIn: getLoginState().user !== null
        };

        this.logIn = this.logIn.bind(this);
    }

    logIn(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();
        this.setState({ loading: true });
        signIn(this.state.email, this.state.password).then(
            () => this.setState({ loggedIn: true }),
            () => {
                this.setState({
                    loading: false,
                    error: true
                });
            });
    }

    render() {
        if (this.state.loggedIn) {
            return <Redirect to="/"/>;
        }

        const { formatMessage } = this.props.intl;

        return (
            <div className={this.props.classes.root}>
                <Paper className={this.props.classes.paper}>
                    <Typography
                        type="display1"
                        gutterBottom={true}
                        className={this.props.classes.tempLogo}
                    >
                        RNDL
                    </Typography>
                    {this.state.error &&
                    <Typography type="body1"><FormattedMessage id="login.invalidCredentials"/></Typography>}
                    <form onSubmit={this.logIn} className={this.props.classes.form}>
                        <TextField
                            required={true}
                            autoFocus={true}
                            label={formatMessage({ id: 'login.emailAddressLabel' })}
                            value={this.state.email}
                            type="email"
                            onChange={event => this.setState({ email: event.target.value })}
                        />
                        <TextField
                            required={true}
                            label={formatMessage({ id: 'login.passwordLabel' })}
                            value={this.state.password}
                            type="password"
                            onChange={event => this.setState({ password: event.target.value })}
                        />
                        <Button
                            raised={true}
                            color="primary"
                            disabled={this.state.loading}
                            type="submit"
                            style={{ position: 'relative' }}
                        >
                            <FormattedMessage id="login.signInLabel"/>
                            {this.state.loading && <CircularProgress
                                size={24}
                                style={{
                                    position: 'absolute',
                                    right: '8px',
                                    top: 'calc(50% - 12px)'
                                }}
                            />}
                        </Button>
                    </form>
                </Paper>
            </div>
        );
    }
}

export default injectIntl(withStyles(loginStyle)(LogInRoute)) as React.ComponentClass<Props>;