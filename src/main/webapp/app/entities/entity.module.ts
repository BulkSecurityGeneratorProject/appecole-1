import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'ecole',
                loadChildren: './ecole/ecole.module#AppEcoleEcoleModule'
            },
            {
                path: 'notification',
                loadChildren: './notification/notification.module#AppEcoleNotificationModule'
            },
            {
                path: 'niveau',
                loadChildren: './niveau/niveau.module#AppEcoleNiveauModule'
            },
            {
                path: 'matiere',
                loadChildren: './matiere/matiere.module#AppEcoleMatiereModule'
            },
            {
                path: 'note',
                loadChildren: './note/note.module#AppEcoleNoteModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppEcoleEntityModule {}
