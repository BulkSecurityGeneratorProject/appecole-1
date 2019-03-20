import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMatiere } from 'app/shared/model/matiere.model';

@Component({
    selector: 'jhi-matiere-detail',
    templateUrl: './matiere-detail.component.html'
})
export class MatiereDetailComponent implements OnInit {
    matiere: IMatiere;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ matiere }) => {
            this.matiere = matiere;
        });
    }

    previousState() {
        window.history.back();
    }
}
