import { IUser } from 'app/core/user/user.model';

export interface IEcole {
    id?: number;
    nom?: string;
    adresse?: string;
    ville?: string;
    telephone?: string;
    user?: IUser;
}

export class Ecole implements IEcole {
    constructor(
        public id?: number,
        public nom?: string,
        public adresse?: string,
        public ville?: string,
        public telephone?: string,
        public user?: IUser
    ) {}
}
