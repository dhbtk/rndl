import 'url-search-params-polyfill';
import Vehicle from '../entities/vehicle/Vehicle';
import Page from '../entities/Page';
import { authFetch } from '../apiFetch';

export const listVehiclesFiltered = (filter: string | null, page: number = 1, size: number = 15): Promise<Page<Vehicle>> => {
    const params = new URLSearchParams();
    if (filter !== null) {
        params.set('filter', filter);
    }
    params.set('page', page.toString());
    params.set('size', size.toString());
    return authFetch(`/api/vehicles?${params.toString()}`);
};

export const showVehicleDetail = (id: number): Promise<Vehicle> => {
    return authFetch(`/api/vehicles/${id}`);
};