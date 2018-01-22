import { listTripsFiltered, showTripDetail } from './TripService';

jest.mock('../apiFetch', () => ({
    authFetch: (query: any) => query
}));

describe('listTripsFiltered', () => {
    it(`does not send vehicleId if parameter is missing`, () => {
        const result = listTripsFiltered(2018, 1) as any;
        expect(result).toContain('year=2018');
        expect(result).toContain('month=1');
        expect(result).not.toContain('vehicleId');
    });
    it('sends vehicleId if it is passed', () => {
        const result = listTripsFiltered(2018, 1, 1) as any;
        expect(result).toContain('vehicleId');
    });
});

describe('showTripDetail', () => {
    it('generates the correct URL', () => {
        expect(showTripDetail(1)).toEqual('/api/trips/1');
    });
});