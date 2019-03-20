import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IMatiere } from 'app/shared/model/matiere.model';
import { AccountService } from 'app/core';
import { MatiereService } from './matiere.service';

@Component({
    selector: 'jhi-matiere',
    templateUrl: './matiere.component.html'
})
export class MatiereComponent implements OnInit, OnDestroy {
    matieres: IMatiere[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected matiereService: MatiereService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.matiereService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<IMatiere[]>) => res.ok),
                    map((res: HttpResponse<IMatiere[]>) => res.body)
                )
                .subscribe((res: IMatiere[]) => (this.matieres = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.matiereService
            .query()
            .pipe(
                filter((res: HttpResponse<IMatiere[]>) => res.ok),
                map((res: HttpResponse<IMatiere[]>) => res.body)
            )
            .subscribe(
                (res: IMatiere[]) => {
                    this.matieres = res;
                    this.currentSearch = '';
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInMatieres();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IMatiere) {
        return item.id;
    }

    registerChangeInMatieres() {
        this.eventSubscriber = this.eventManager.subscribe('matiereListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
