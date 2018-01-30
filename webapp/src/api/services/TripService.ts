import 'url-search-params-polyfill';
import Trip from '../entities/vehicle/Trip';
import { authFetch } from '../apiFetch';
import GroupedTripList from '../entities/vehicle/GroupedTripList';

export const listTripsFiltered = (year: number, month: number, vehicleId: number | null = null): Promise<GroupedTripList[]> => {
    const params = new URLSearchParams();
    params.set('year', year.toString());
    params.set('month', month.toString());
    if (vehicleId !== null) {
        params.set('vehicleId', vehicleId.toString());
    }
    return authFetch(`/api/trips?${params}`);
};

export const showTripDetail = (id: number): Promise<Trip> => {
    return authFetch(`/api/trips/${id}`);
};