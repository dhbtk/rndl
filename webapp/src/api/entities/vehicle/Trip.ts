import Entity from '../Entity';
import Vehicle from './Vehicle';
import Entry from './Entry';

export default interface Trip extends Entity {
    startTimestamp: number;
    duration: string;
    distance: number;
    averageSpeed: number;
    maximumSpeed: number;
    economy: number;
    fuelUsed: number;
    vehicleId: number;

    vehicle: Vehicle;
    entries: Entry[];
    startTime: string;
}