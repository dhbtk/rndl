import { LoginState } from '../api/services/LoginService';
import { Redirect, Route } from 'react-router';
import * as React from 'react';
import { WithStyles } from 'material-ui';
import { StyleRules, Theme } from 'material-ui/styles';
import withStyles from 'material-ui/styles/withStyles';
import NavigationDrawer from './root/NavigationDrawer';
import DashboardRoute from './dashboard/DashboardRoute';
import User from '../api/entities/user/User';
import TripListRoute from './trips/TripListRoute';

export interface Props {
    loginState: LoginState;
}

type StyleProps = Props & WithStyles<string>;

const styles = (theme: Theme): StyleRules => ({
    root: {
        display: 'flex'
    },
    content: {
        flex: 1,
        width: 'calc(100% - 300px)', // TODO responsiveness: only if drawer is fixed open
        marginLeft: '300px' // same as above
    }
});

const RootRoute = ({ loginState, classes }: StyleProps) => {
    if (!loginState.user) {
        return <Redirect to="/login"/>;
    } else {
        return (
            <div className={classes.root}>
                <NavigationDrawer user={loginState.user} open={true}/>
                <div className={classes.content}>
                    <Route exact={true} path="/" render={() => <DashboardRoute user={loginState.user as User}/>}/>

                    <Route exact={true} path="/trips" component={TripListRoute}/>
                    <Route exact={true} path="/trips/:year/:month" component={TripListRoute}/>
                    <Route exact={true} path="/trips/:year/:month/:vehicleId" component={TripListRoute}/>
                </div>
            </div>
        );
    }
};

export default withStyles(styles)(RootRoute) as React.StatelessComponent<Props>;