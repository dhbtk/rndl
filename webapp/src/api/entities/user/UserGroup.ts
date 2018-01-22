import Entity from '../Entity';
import User from './';
import UserGroupMembership from './UserGroupMembership';

export default interface UserGroup extends Entity {
    ownerId?: number;
    name?: string;
    owner?: User;
    userGroupMemberships?: UserGroupMembership[];
}