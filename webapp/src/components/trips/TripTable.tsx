import GroupedTripList from '../../api/entities/vehicle/GroupedTripList';
import * as React from 'react';
import { FormattedDate, FormattedMessage, FormattedNumber, InjectedIntlProps, injectIntl } from 'react-intl';
import Typography from 'material-ui/Typography';
import Divider from 'material-ui/Divider';
import { StyleRules, Theme, withStyles } from 'material-ui/styles';
import { Table, TableBody, TableCell, TableHead, TableRow, WithStyles } from 'material-ui';
import { Link } from 'react-router-dom';

export interface Props {
    group: GroupedTripList;
}

const styles = (theme: Theme): StyleRules => ({
    title: {
        padding: theme.spacing.unit * 2,
    },
    table: {
        width: '100%'
    }
});

class TripTable extends React.Component<Props & InjectedIntlProps & WithStyles<string>> {
    render() {
        return (
            <div>
                <Typography type="title" className={this.props.classes.title}>
                    <FormattedDate value={this.props.group.date} year="numeric" month="long" day="numeric"/>
                </Typography>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell><FormattedMessage id="tripList.timeHeader"/></TableCell>
                            <TableCell><FormattedMessage id="tripList.durationHeader"/></TableCell>
                            <TableCell><FormattedMessage id="tripList.vehicleHeader"/></TableCell>
                            <TableCell numeric={true}><FormattedMessage id="tripList.distanceHeader"/></TableCell>
                            <TableCell numeric={true}><FormattedMessage id="tripList.economyHeader"/></TableCell>
                            <TableCell numeric={true}><FormattedMessage id="tripList.averageSpeedHeader"/></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {this.props.group.trips.map(trip => (
                            <TableRow key={trip.id}>
                                <TableCell>
                                    <Link to={`/trips/${trip.id}`}>
                                        <FormattedDate
                                            value={trip.startTime}
                                            hour="numeric"
                                            minute="numeric"
                                        />
                                    </Link>
                                </TableCell>
                                <TableCell>
                                    {trip.duration}
                                </TableCell>
                                <TableCell>
                                    <Link to={`/vehicles/${trip.vehicleId}`}>
                                        {trip.vehicle.name} {trip.vehicle.licensePlate !== null && `&bull; ${trip.vehicle.licensePlate}`}
                                    </Link>
                                </TableCell>
                                <TableCell numeric={true}>
                                    {trip.distance === null ? '-' :
                                        <FormattedMessage
                                            id="tripList.distanceLabel"
                                            values={{
                                                km:
                                                    <FormattedNumber
                                                        style="decimal"
                                                        value={+(trip.distance / 1000).toFixed(1)}
                                                    />
                                            }}
                                        />}
                                </TableCell>
                                <TableCell numeric={true}>
                                    {trip.economy === null ? '-' :
                                        <FormattedMessage
                                            id="tripList.economyLabel"
                                            values={{
                                                economy: <FormattedNumber style="decimal" value={trip.economy}/>
                                            }}
                                        />
                                    }
                                </TableCell>
                                <TableCell numeric={true}>
                                    {trip.averageSpeed === null ? '-' :
                                        <FormattedMessage
                                            id="tripList.speedLabel"
                                            values={{
                                                speed: <FormattedNumber style="decimal" value={trip.averageSpeed}/>
                                            }}
                                        />
                                    }
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
                <Divider/>
            </div>
        );
    }
}

export default injectIntl<Props>(withStyles(styles)(TripTable));