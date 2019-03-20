import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IEcole } from 'app/shared/model/ecole.model';
import { AccountService } from 'app/core';
import { EcoleService } from './ecole.service';

@Component({
    selector: 'jhi-ecole',
    templateUrl: './ecole.component.html'
})
export class EcoleComponent implements OnInit, OnDestroy {
    ecoles: IEcole[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected ecoleService: EcoleService,
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
            this.ecoleService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<IEcole[]>) => res.ok),
                    map((res: HttpResponse<IEcole[]>) => res.body)
                )
                .subscribe((res: IEcole[]) => (this.ecoles = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.ecoleService
            .query()
            .pipe(
                filter((res: HttpResponse<IEcole[]>) => res.ok),
                map((res: HttpResponse<IEcole[]>) => res.body)
            )
            .subscribe(
                (res: IEcole[]) => {
                    this.ecoles = res;
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
        this.registerChangeInEcoles();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IEcole) {
        return item.id;
    }

    registerChangeInEcoles() {
        this.eventSubscriber = this.eventManager.subscribe('ecoleListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
