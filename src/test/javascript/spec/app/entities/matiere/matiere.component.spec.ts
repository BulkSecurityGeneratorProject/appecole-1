/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { AppEcoleTestModule } from '../../../test.module';
import { MatiereComponent } from 'app/entities/matiere/matiere.component';
import { MatiereService } from 'app/entities/matiere/matiere.service';
import { Matiere } from 'app/shared/model/matiere.model';

describe('Component Tests', () => {
    describe('Matiere Management Component', () => {
        let comp: MatiereComponent;
        let fixture: ComponentFixture<MatiereComponent>;
        let service: MatiereService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AppEcoleTestModule],
                declarations: [MatiereComponent],
                providers: []
            })
                .overrideTemplate(MatiereComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(MatiereComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MatiereService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Matiere(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.matieres[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
