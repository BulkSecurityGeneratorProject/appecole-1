import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { INiveau } from 'app/shared/model/niveau.model';
import { NiveauService } from './niveau.service';
import { IMatiere } from 'app/shared/model/matiere.model';
import { MatiereService } from 'app/entities/matiere';

@Component({
    selector: 'jhi-niveau-update',
    templateUrl: './niveau-update.component.html'
})
export class NiveauUpdateComponent implements OnInit {
    niveau: INiveau;
    isSaving: boolean;

    matieres: IMatiere[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected niveauService: NiveauService,
        protected matiereService: MatiereService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ niveau }) => {
            this.niveau = niveau;
        });
        this.matiereService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IMatiere[]>) => mayBeOk.ok),
                map((response: HttpResponse<IMatiere[]>) => response.body)
            )
            .subscribe((res: IMatiere[]) => (this.matieres = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.niveau.id !== undefined) {
            this.subscribeToSaveResponse(this.niveauService.update(this.niveau));
        } else {
            this.subscribeToSaveResponse(this.niveauService.create(this.niveau));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<INiveau>>) {
        result.subscribe((res: HttpResponse<INiveau>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackMatiereById(index: number, item: IMatiere) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
