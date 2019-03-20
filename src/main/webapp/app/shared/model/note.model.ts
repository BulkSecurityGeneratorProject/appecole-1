import { Moment } from 'moment';
import { IMatiere } from 'app/shared/model/matiere.model';
import { IUser } from 'app/core/user/user.model';

export interface INote {
    id?: number;
    valeur?: number;
    dateControle?: Moment;
    matiere?: IMatiere;
    user?: IUser;
}

export class Note implements INote {
    constructor(public id?: number, public valeur?: number, public dateControle?: Moment, public matiere?: IMatiere, public user?: IUser) {}
}
