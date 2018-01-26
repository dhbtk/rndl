import User from '../../api/entities/user/User';
import { WithStyles } from 'material-ui';
import { StyleRules, Theme } from 'material-ui/styles';
import * as React from 'react';
import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';
import withStyles from 'material-ui/styles/withStyles';
import PaddedContainer from '../common/PaddedContainer';
import PaddedAppBar from '../common/PaddedAppBar';

export interface Props {
    user: User;
}

type StyleProps = Props & WithStyles<string>;

const styles = (theme: Theme): StyleRules => ({
    main: {
        margin: '0 auto',
        maxWidth: '900px'
    }
});

export class DashboardRoute extends React.Component<StyleProps> {
    render() {
        const { user } = this.props;
        return (
            <div>
                <PaddedAppBar position="fixed">
                    <Toolbar>
                        <Typography type="title">
                            RNDL
                        </Typography>
                    </Toolbar>
                </PaddedAppBar>
                <PaddedContainer>
                    <Typography type="display1">
                        Hello {user.fullName}!
                    </Typography>
                </PaddedContainer>
            </div>
        );
    }
}

export default withStyles(styles)(DashboardRoute) as React.ComponentClass<Props>;