import Entity from '../Entity';
import User from './';
import UserGroup from './UserGroup';

export default interface UserGroupMembership extends Entity {
    userId?: number;
    userGroupId?: number;
    administrator?: boolean;
    userGroup?: UserGroup;
    user?: User;
}