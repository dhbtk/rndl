import Entity from '../Entity';

export default interface User extends Entity {
    email?: string;
    fullName?: string;
    userGroupMemberships?: any[];
}