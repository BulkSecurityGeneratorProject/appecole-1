import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { AppEcoleSharedModule } from 'app/shared';
import {
    MatiereComponent,
    MatiereDetailComponent,
    MatiereUpdateComponent,
    MatiereDeletePopupComponent,
    MatiereDeleteDialogComponent,
    matiereRoute,
    matierePopupRoute
} from './';

const ENTITY_STATES = [...matiereRoute, ...matierePopupRoute];

@NgModule({
    imports: [AppEcoleSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MatiereComponent,
        MatiereDetailComponent,
        MatiereUpdateComponent,
        MatiereDeleteDialogComponent,
        MatiereDeletePopupComponent
    ],
    entryComponents: [MatiereComponent, MatiereUpdateComponent, MatiereDeleteDialogComponent, MatiereDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppEcoleMatiereModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
