import GroupedTripList from '../../api/entities/vehicle/GroupedTripList';
import { CardContent, CircularProgress, WithStyles } from 'material-ui';
import { StyleRules, Theme } from 'material-ui/styles';
import * as React from 'react';
import Typography from 'material-ui/Typography';
import Card from 'material-ui/Card';
import withStyles from 'material-ui/styles/withStyles';
import { FormattedMessage, FormattedNumber, InjectedIntlProps, injectIntl } from 'react-intl';

export interface Props {
    trips: GroupedTripList[];
    loading: boolean;
}

type InternalProps = Props & WithStyles<string> & InjectedIntlProps;

const styles = (theme: Theme): StyleRules => ({
    loadingCard: {
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center'
    },
    root: {
        height: 200
    }
});

class MonthlyDistanceCard extends React.Component<InternalProps> {
    render() {
        const { trips, loading, classes } = this.props;
        if (loading) {
            return (
                <Card classes={{ root: this.props.classes.root }}>
                    <CardContent className={classes.loadingCard}>
                        <CircularProgress size={64}/>
                    </CardContent>
                </Card>
            );
        } else {
            const distance = +(trips.map(day => day.trips.map(t => (t.distance as number)).reduce((a, b) => a + b, 0))
                .reduce((a, b) => a + b, 0) / 1000).toFixed(2);
            return (
                <Card classes={{ root: this.props.classes.root }}>
                    <CardContent>
                        <Typography type="display2">
                            <FormattedMessage
                                id="tripList.distanceLabel"
                                values={{
                                    km: <FormattedNumber style="decimal" value={distance}/>
                                }}
                            />
                        </Typography>
                        <Typography>
                            <FormattedMessage id="tripList.traveledThisMonth"/>
                        </Typography>
                    </CardContent>
                </Card>
            );
        }
    }
}

export default injectIntl(withStyles(styles)(MonthlyDistanceCard)) as React.ComponentClass<Props>;