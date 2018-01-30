import { match } from 'react-router';
import { StyleRules, Theme } from 'material-ui/styles';
import * as React from 'react';
import { WithStyles } from 'material-ui';
import PaddedAppBar from '../common/PaddedAppBar';
import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';
import PaddedContainer from '../common/PaddedContainer';
import withStyles from 'material-ui/styles/withStyles';
import GroupedTripList from '../../api/entities/vehicle/GroupedTripList';
import { listTripsFiltered } from '../../api/services/TripService';
import Paper from 'material-ui/Paper';
import TripTable from './TripTable';

interface Params {
    year?: string;
    month?: string;
    vehicleId?: string;
}

export interface Props {
    match: match<Params>;
}

interface State {
    year: number;
    month: number;
    vehicleId?: number;
    loadingTrips: boolean;
    trips?: GroupedTripList[];
}

const styles = (theme: Theme): StyleRules => ({});

type StyleProps = Props & WithStyles<string>;

class TripListRoute extends React.Component<StyleProps, State> {
    constructor(props: StyleProps) {
        super(props);
        this.state = {
            ...this.stateFromProps(props) as State,
            loadingTrips: false,
            trips: []
        };
    }

    componentWillMount() {
        this.fetchTripList(this.state);
    }

    stateFromProps(props: Props): Partial<State> {
        const { params } = props.match;
        return ({
            year: params.year ? parseInt(params.year, 10) : new Date().getFullYear(),
            month: params.month ? parseInt(params.month, 10) : new Date().getMonth() + 1,
            vehicleId: params.vehicleId != null ? parseInt(params.vehicleId, 10) : undefined
        });
    }

    componentWillReceiveProps(newProps: Props) {
        const newState = this.stateFromProps(newProps) as State;
        this.setState(newState);
        this.fetchTripList(Object.assign({}, this.state, newState));
    }

    fetchTripList({ year, month, vehicleId, loadingTrips }: State) {
        if (!loadingTrips) {
            this.setState({ loadingTrips: true });
            listTripsFiltered(year, month, vehicleId).then(trips => this.setState({ trips }), () => this.setState({ loadingTrips: false }))
                .then(() => this.setState({ loadingTrips: false }));
        }
    }

    render() {
        return (
            <div>
                <PaddedAppBar position="fixed">
                    <Toolbar>
                        <Typography type="title">Trips</Typography>
                    </Toolbar>
                    <Toolbar>
                        <Typography>{this.state.loadingTrips && 'Loading...'}</Typography>
                    </Toolbar>
                </PaddedAppBar>
                <PaddedContainer height={2}>
                    <Paper>
                        {(this.state.trips as GroupedTripList[]).map(group => <TripTable key={group.date}
                                                                                         group={group}/>)}
                    </Paper>
                </PaddedContainer>
            </div>
        );
    }
}

export default withStyles(styles)(TripListRoute) as React.ComponentClass<Props>;