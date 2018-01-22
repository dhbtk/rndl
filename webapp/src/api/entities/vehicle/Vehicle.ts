import Entity from '../Entity';
import UserGroup from '../user/UserGroup';
import Entry from './Entry';

export default interface Vehicle extends Entity {
    name?: string;
    torqueId?: string;
    licensePlate?: string;
    gearRatios?: number[];
    finalDrive?: number;
    tireDiameter?: number;
    userGroupId?: number;
    latestEntry?: Entry;
    userGroup?: UserGroup;
}