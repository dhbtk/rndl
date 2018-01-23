import Entity from '../Entity';
import UserGroupMembership from './UserGroupMembership';

export default interface User extends Entity {
    email?: string;
    fullName?: string;
    userGroupMemberships?: UserGroupMembership[];
}