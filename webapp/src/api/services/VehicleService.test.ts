import { listVehiclesFiltered, showVehicleDetail } from './VehicleService';

jest.mock('../apiFetch', () => ({
    authFetch: (query: string) => query
}));

describe('listVehiclesFiltered', () => {
    it('adds the filter string correctly', () => {
        const result = listVehiclesFiltered('test');
        expect(result).toContain('/api/vehicles');
        expect(result).toContain('filter=test');
    });
});

describe('showVehicleDetail', () => {
    it('generates the correct URL', () => {
        const result = showVehicleDetail(2);
        expect(result).toEqual('/api/vehicles/2');
    });
});