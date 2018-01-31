import { match } from 'react-router';
import { StyleRules, Theme } from 'material-ui/styles';
import * as React from 'react';
import { CircularProgress, WithStyles } from 'material-ui';
import PaddedAppBar from '../common/PaddedAppBar';
import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';
import PaddedContainer from '../common/PaddedContainer';
import withStyles from 'material-ui/styles/withStyles';
import GroupedTripList from '../../api/entities/vehicle/GroupedTripList';
import { listTripsFiltered } from '../../api/services/TripService';
import Paper from 'material-ui/Paper';
import TripTable from './TripTable';
import { FormattedDate } from 'react-intl';
import IconButton from 'material-ui/IconButton';
import Icon from 'material-ui/Icon';
import { Link } from 'react-router-dom';

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

const styles = (theme: Theme): StyleRules => ({
    root: {
        position: 'relative'
    },
    overlay: {
        position: 'absolute',
        width: '100%',
        height: '100vh',
        background: 'rgba(255, 255, 255, 0.54)',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        top: 0,
        left: 0
    }
});

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

    urlFor([year, month]: [number, number]) {
        return `/trips/${year}/${month}${this.state.vehicleId ?
            '/' + this.state.vehicleId : ''}`;
    }

    nextMonth(year: number, month: number): [number, number] {
        if (month === 12) {
            return [year + 1, 1];
        } else {
            return [year, month + 1];
        }
    }

    previousMonth(year: number, month: number): [number, number] {
        if (month === 1) {
            return [year - 1, 12];
        } else {
            return [year, month - 1];
        }
    }

    render() {
        return (
            <div className={this.props.classes.root}>
                <PaddedAppBar position="fixed">
                    <Toolbar>
                        <Typography type="title">Trips</Typography>
                    </Toolbar>
                    <Toolbar>
                        <IconButton component={props => <Link
                            to={this.urlFor(this.previousMonth(this.state.year, this.state.month))} {...props} />}>
                            <Icon>navigate_before</Icon>
                        </IconButton>
                        <IconButton component={props => <Link
                            to={this.urlFor(this.nextMonth(this.state.year, this.state.month))} {...props} />}>
                            <Icon>navigate_next</Icon>
                        </IconButton>
                        <Typography type="title">
                            <FormattedDate
                                value={new Date(this.state.year, this.state.month, 0)}
                                year="numeric"
                                month="long"
                            />
                        </Typography>
                    </Toolbar>
                </PaddedAppBar>
                <PaddedContainer height={2}>
                    <Paper>
                        {(this.state.trips as GroupedTripList[]).map(group => (
                            <TripTable
                                key={group.date}
                                group={group}
                            />))}
                    </Paper>
                </PaddedContainer>
                {this.state.loadingTrips && (
                    <div className={this.props.classes.overlay}>
                        <CircularProgress size={64}/>
                    </div>
                )}
            </div>
        );
    }
}

export default withStyles(styles)(TripListRoute) as React.ComponentClass<Props>;
