import { IUser } from 'app/core/user/user.model';

export const enum TypeNotification {
    ALERTE = 'ALERTE',
    EVENEMENT = 'EVENEMENT',
    INFO = 'INFO'
}

export interface INotification {
    id?: number;
    titre?: string;
    description?: string;
    type?: TypeNotification;
    user?: IUser;
}

export class Notification implements INotification {
    constructor(
        public id?: number,
        public titre?: string,
        public description?: string,
        public type?: TypeNotification,
        public user?: IUser
    ) {}
}
