import GroupedTripList from '../../api/entities/vehicle/GroupedTripList';
import * as React from 'react';
import { FormattedDate, FormattedMessage, FormattedNumber, InjectedIntlProps, injectIntl } from 'react-intl';
import Typography from 'material-ui/Typography';
import Divider from 'material-ui/Divider';
import { StyleRules, Theme, withStyles } from 'material-ui/styles';
import { Table, TableBody, TableCell, TableHead, TableRow, WithStyles } from 'material-ui';

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
                            <TableCell>Hora</TableCell>
                            <TableCell>Duração</TableCell>
                            <TableCell>Veículo</TableCell>
                            <TableCell>Distância</TableCell>
                            <TableCell>Economia</TableCell>
                            <TableCell>Velocidade média</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {this.props.group.trips.map(trip => (
                            <TableRow key={trip.id}>
                                <TableCell>
                                    <FormattedDate
                                        value={trip.startTime}
                                        hour="numeric"
                                        minute="numeric"
                                    />
                                </TableCell>
                                <TableCell>
                                    {trip.duration}
                                </TableCell>
                                <TableCell>
                                    {trip.vehicle.name}
                                </TableCell>
                                <TableCell>
                                    <FormattedMessage
                                        id="tripList.distanceLabel"
                                        values={{
                                            km: <FormattedNumber style="decimal" value={trip.distance / 1000}/>
                                        }}
                                    />
                                </TableCell>
                                <TableCell>
                                    <FormattedMessage
                                        id="tripList.economyLabel"
                                        values={{
                                            economy: <FormattedNumber style="decimal" value={trip.economy}/>
                                        }}
                                    />
                                </TableCell>
                                <TableCell>
                                    <FormattedMessage
                                        id="tripList.speedLabel"
                                        values={{
                                            speed: <FormattedNumber style="decimal" value={trip.averageSpeed}/>
                                        }}
                                    />
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

export default injectIntl(withStyles(styles)(TripTable)) as React.ComponentClass<Props>;