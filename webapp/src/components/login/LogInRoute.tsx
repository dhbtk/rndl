import * as React from 'react';
import { FormEvent } from 'react';
import { getLoginState, logIn, LoginState } from '../../api/services/LoginService';
import { StyleRules, Theme } from 'material-ui/styles';
import withStyles from 'material-ui/styles/withStyles';
import { Button, CircularProgress, TextField, Typography, WithStyles } from 'material-ui';
import Paper from 'material-ui/Paper';
import { Redirect } from 'react-router';

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
        background: (theme.palette.primary as any).main,
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

type StyleProps = Props & WithStyles<string>;

class LogInRoute extends React.Component<StyleProps, State> {
    constructor(props: StyleProps) {
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
        logIn(this.state.email, this.state.password).then(
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

        return (
            <div className={this.props.classes.root}>
                <Paper className={this.props.classes.paper}>
                    <Typography type="display1" gutterBottom={true}
                                className={this.props.classes.tempLogo}>RNDL</Typography>
                    {this.state.error && <Typography type="body1">Usuário ou senha inválidos.</Typography>}
                    <form onSubmit={this.logIn} className={this.props.classes.form}>
                        <TextField
                            required={true}
                            autoFocus={true}
                            label="Email address"
                            value={this.state.email}
                            type="email"
                            onChange={event => this.setState({ email: event.target.value })}
                        />
                        <TextField
                            required={true}
                            label="Password"
                            value={this.state.password}
                            type="password"
                            onChange={event => this.setState({ password: event.target.value })}
                        />
                        <Button raised={true} color="primary" disabled={this.state.loading} type="submit"
                                style={{ position: 'relative' }}>
                            Sign in
                            {this.state.loading && <CircularProgress size={24} style={{
                                position: 'absolute',
                                right: '8px',
                                top: 'calc(50% - 12px)'
                            }}/>}
                        </Button>
                    </form>
                </Paper>
            </div>
        );
    }
}

export default withStyles(loginStyle)(LogInRoute) as React.ComponentClass<Props>;