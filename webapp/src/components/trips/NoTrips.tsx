import * as React from 'react';
import { WithStyles } from 'material-ui';
import { StyleRules, Theme } from 'material-ui/styles';
import withStyles from 'material-ui/styles/withStyles';
import Typography from 'material-ui/Typography';
import { FormattedMessage } from 'react-intl';

const styles = (theme: Theme): StyleRules => ({
    root: {
        padding: 32,
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        '& > *': {
            paddingTop: 16,
            paddingBottom: 16
        }
    }
});

function NoTrips({ classes }: WithStyles) {
    return (
        <div className={classes.root}>
            <Typography type="display1">
                <FormattedMessage id="noTrips.noTripsHeader"/>
            </Typography>
            <Typography>
                <FormattedMessage id="noTrips.noTripsMessage"/>
            </Typography>
        </div>
    );
}

export default withStyles(styles)(NoTrips) as React.StatelessComponent<{}>;