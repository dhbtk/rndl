import { listVehiclesFiltered, showVehicleDetail } from './VehicleService';

jest.mock('../apiFetch', () => ({
    authFetch: (query: any) => query
}));

describe('listVehiclesFiltered', () => {
    it('adds the filter string correctly', () => {
        const result = listVehiclesFiltered('test') as any;
        expect(result).toContain('/api/vehicles');
        expect(result).toContain('filter=test');
    });
});

describe('showVehicleDetail', () => {
    it('generates the correct URL', () => {
        const result = showVehicleDetail(2) as any;
        expect(result).toEqual('/api/vehicles/2');
    });
});