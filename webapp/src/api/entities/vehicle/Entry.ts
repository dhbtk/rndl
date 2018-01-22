import Entity from '../Entity';
import { Point } from 'geojson';
import Trip from './Trip';

export default interface Entry extends Entity {
    tripId?: number;
    deviceTime?: string;
    coordinates?: Point;
    gpsSpeed?: number;
    gpsAltitude?: number;
    rpm?: number;
    economy?: number;
    speed?: number;
    throttle?: number;
    instantEconomy?: number;
    fuelFlow?: number;
    fuelUsed?: number;
    trip?: Trip;
}