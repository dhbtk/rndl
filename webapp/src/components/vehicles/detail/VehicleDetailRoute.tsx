import { match } from 'react-router';
import Vehicle from '../../../api/entities/vehicle/Vehicle';
import { StyleRules, Theme } from 'material-ui/styles';
import { WithStyles } from 'material-ui';
import * as React from 'react';
import LoadingOverlay from '../../common/LoadingOverlay';
import PaddedAppBar from '../../common/PaddedAppBar';
import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';
import withStyles from 'material-ui/styles/withStyles';
import { showVehicleDetail } from '../../../api/services/VehicleService';

export interface Props {
    match: match<{id: string}>;
}

interface State {
    loading: boolean;
    vehicle: Vehicle | null;
}

const styles = (theme: Theme): StyleRules => ({
    root: {
        position: 'relative'
    }
});

class VehicleDetailRoute extends React.Component<Props & WithStyles<string>, State> {
    constructor(props: Props & WithStyles<string>) {
        super(props);
        this.state = {
            loading: true,
            vehicle: null
        };
    }

    componentWillMount() {
        this.loadDetails(parseInt(this.props.match.params.id, 10));
    }

    loadDetails(id: number) {
        showVehicleDetail(id)
            .then(
                vehicle => this.setState({ vehicle, loading: false }),
                () => this.setState({ loading: false }));
    }

    componentWillReceiveProps(newProps: Props & WithStyles<string>) {
        this.loadDetails(parseInt(newProps.match.params.id, 10));
    }

    render() {
        const { classes } = this.props;
        const {loading, vehicle} = this.state;
        return (
            <div className={classes.root}>
                <PaddedAppBar>
                    <Toolbar>
                        {vehicle !== null && (
                            <Typography type="title">
                                {vehicle.name} {vehicle.licensePlate !== null && `&bull; ${vehicle.licensePlate}`}
                            </Typography>
                        )}
                    </Toolbar>
                </PaddedAppBar>
                {loading && <LoadingOverlay />}
            </div>
        );
    }
}

export default withStyles(styles)<{}>(VehicleDetailRoute);