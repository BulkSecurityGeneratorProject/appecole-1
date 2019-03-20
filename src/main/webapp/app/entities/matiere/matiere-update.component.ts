import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IMatiere } from 'app/shared/model/matiere.model';
import { MatiereService } from './matiere.service';
import { INiveau } from 'app/shared/model/niveau.model';
import { NiveauService } from 'app/entities/niveau';

@Component({
    selector: 'jhi-matiere-update',
    templateUrl: './matiere-update.component.html'
})
export class MatiereUpdateComponent implements OnInit {
    matiere: IMatiere;
    isSaving: boolean;

    niveaus: INiveau[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected matiereService: MatiereService,
        protected niveauService: NiveauService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ matiere }) => {
            this.matiere = matiere;
        });
        this.niveauService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<INiveau[]>) => mayBeOk.ok),
                map((response: HttpResponse<INiveau[]>) => response.body)
            )
            .subscribe((res: INiveau[]) => (this.niveaus = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.matiere.id !== undefined) {
            this.subscribeToSaveResponse(this.matiereService.update(this.matiere));
        } else {
            this.subscribeToSaveResponse(this.matiereService.create(this.matiere));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IMatiere>>) {
        result.subscribe((res: HttpResponse<IMatiere>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackNiveauById(index: number, item: INiveau) {
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
