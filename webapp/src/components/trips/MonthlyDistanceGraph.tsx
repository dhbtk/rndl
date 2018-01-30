import GroupedTripList from '../../api/entities/vehicle/GroupedTripList';
import Trip from '../../api/entities/vehicle/Trip';
import { CardContent, WithStyles } from 'material-ui';
import { StyleRules, Theme } from 'material-ui/styles';
import * as React from 'react';
import Typography from 'material-ui/Typography';
import Card from 'material-ui/Card';
import withStyles from 'material-ui/styles/withStyles';
import { FormattedMessage, InjectedIntlProps, injectIntl } from 'react-intl';

export interface Props {
    trips: GroupedTripList[];
    loading: boolean;
}

type InternalProps = Props & WithStyles<string> & InjectedIntlProps;

const styles = (theme: Theme): StyleRules => ({
    root: {
        height: 200
    }
});

type TripDistances = {
    day: number;
    distance: number;
}[];

function groupTrips(groups: GroupedTripList[]): TripDistances {
    if (!groups.length) {
        return [];
    }
    const [year, month] = (groups[0].date as string).split('-').map(i => parseInt(i, 10));
    console.log('year: ', year, 'month: ', month);
    const daysThisMonth = new Date(year, month, 0).getDate();
    const distances: number[] = [];
    groups.forEach(group => {
        const day = parseInt((group.date as string).split('-')[2], 10);
        distances[day] = (group.trips as Trip[]).map(trip => (trip.distance as number) / 1000).reduce((a, b) => a + b, 0);
    });
    for (let i = 1; i <= daysThisMonth; i++) {
        if (distances[i] === undefined) {
            distances[i] = 0;
        }
    }
    console.log(distances);
    return distances.filter(i => i !== undefined).map((distance, i) => ({ day: i, distance: +distance.toFixed(2) }));
}

class MonthlyDistanceGraph extends React.Component<InternalProps> {
    // chart: Chart | null;

    componentDidMount() {
        const trips = groupTrips(this.props.trips);
        console.log(trips);
        /*this.chart = new Chart('distanceTraveledGraph', {
            type: 'bar',
            data: {
                labels: trips.map(i => i.day.toString()),
                datasets: [{
                    data: trips.map(i => i.distance)
                }]
            },
        });
        */
    }

    componentWillReceiveProps(newProps: InternalProps) {
        /*const trips = groupTrips(newProps.trips);
        if (this.chart !== null) {
            this.chart.data.labels = trips.map(i => i.day.toString());
            (this.chart.data.datasets as ChartDataSets[])[0].data = trips.map(i => i.distance);
            this.chart.update();
        }*/
    }

    render() {
        const t = true;
        if (t) {
            return (
                <Card classes={{ root: this.props.classes.root }}>
                    <CardContent>
                        <Typography>Distances</Typography>
                    </CardContent>
                </Card>
            );
        } else {
            return (
                <Card>
                    <CardContent>
                        <Typography type="headline"><FormattedMessage
                            id="tripList.distanceTraveledThisMonth"/></Typography>
                        <canvas id="distanceTraveledGraph" height="200px" width="400px"/>
                    </CardContent>
                </Card>
            );
        }
    }
}

export default injectIntl(withStyles(styles)(MonthlyDistanceGraph)) as React.ComponentClass<Props>;