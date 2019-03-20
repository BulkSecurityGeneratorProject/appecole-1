import { IMatiere } from 'app/shared/model/matiere.model';

export interface INiveau {
    id?: number;
    titre?: string;
    description?: string;
    ids?: IMatiere[];
}

export class Niveau implements INiveau {
    constructor(public id?: number, public titre?: string, public description?: string, public ids?: IMatiere[]) {}
}
