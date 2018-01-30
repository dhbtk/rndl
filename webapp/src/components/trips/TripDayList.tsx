import GroupedTripList from '../../api/entities/vehicle/GroupedTripList';
import List, { ListItem } from 'material-ui/List';
import * as React from 'react';
import { InjectedIntlProps, injectIntl } from 'react-intl';
import { ListItemText } from 'material-ui';
import Paper from 'material-ui/Paper';

export interface Props {
    trips: GroupedTripList[];
}

class TripDayList extends React.Component<Props & InjectedIntlProps> {
    render() {
        const { trips, intl } = this.props;
        return (
            <Paper>
                <List>
                    {trips.map(trip => (
                        <ListItem button={true} key={trip.date}>
                            <ListItemText
                                primary={intl.formatDate(new Date(trip.date as string), {
                                    year: 'numeric',
                                    month: 'long',
                                    day: 'numeric'
                                })}
                                secondary={intl.formatMessage({ id: 'tripList.tripsOnDay' }, { count: trip.trips.length })}
                            />
                        </ListItem>
                    ))}
                </List>
            </Paper>
        );
    }
}

export default injectIntl(TripDayList) as React.ComponentClass<Props>;