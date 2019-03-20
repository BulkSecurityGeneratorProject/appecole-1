import { INiveau } from 'app/shared/model/niveau.model';
import { INote } from 'app/shared/model/note.model';

export interface IMatiere {
    id?: number;
    titre?: string;
    description?: string;
    niveaus?: INiveau[];
    ids?: INote[];
}

export class Matiere implements IMatiere {
    constructor(public id?: number, public titre?: string, public description?: string, public niveaus?: INiveau[], public ids?: INote[]) {}
}
