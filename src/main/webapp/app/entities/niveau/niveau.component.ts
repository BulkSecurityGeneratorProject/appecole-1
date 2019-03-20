import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { INiveau } from 'app/shared/model/niveau.model';
import { AccountService } from 'app/core';
import { NiveauService } from './niveau.service';

@Component({
    selector: 'jhi-niveau',
    templateUrl: './niveau.component.html'
})
export class NiveauComponent implements OnInit, OnDestroy {
    niveaus: INiveau[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected niveauService: NiveauService,
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
            this.niveauService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<INiveau[]>) => res.ok),
                    map((res: HttpResponse<INiveau[]>) => res.body)
                )
                .subscribe((res: INiveau[]) => (this.niveaus = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.niveauService
            .query()
            .pipe(
                filter((res: HttpResponse<INiveau[]>) => res.ok),
                map((res: HttpResponse<INiveau[]>) => res.body)
            )
            .subscribe(
                (res: INiveau[]) => {
                    this.niveaus = res;
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
        this.registerChangeInNiveaus();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: INiveau) {
        return item.id;
    }

    registerChangeInNiveaus() {
        this.eventSubscriber = this.eventManager.subscribe('niveauListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
