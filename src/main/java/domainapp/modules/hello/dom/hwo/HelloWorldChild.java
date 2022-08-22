package domainapp.modules.hello.dom.hwo;

import java.util.Comparator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Publishing;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.layout.LayoutConstants;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

import domainapp.modules.hello.types.Name;
import domainapp.modules.hello.types.Notes;

@PersistenceCapable(
        schema = "hello",
        identityType = IdentityType.DATASTORE
)
@Unique(
        name = "HelloWorldChild__name__UNQ", members = {"name"}
)
@DatastoreIdentity(strategy = IdGeneratorStrategy.IDENTITY, column = "id")
@Version(strategy= VersionStrategy.VERSION_NUMBER, column ="version")
@Queries(
        @Query(
                name = "findByName",
                value = "SELECT " +
                        "FROM domainapp.modules.hello.dom.hwo.HelloWorldChild " +
                        "WHERE name.indexOf(:name) >= 0"
        )
)
@Named("hello.HelloWorldChild")
@DomainObject()
@DomainObjectLayout()  // causes UI events to be triggered
public class HelloWorldChild implements Comparable<HelloWorldChild> {

    private HelloWorldChild(){}

    public HelloWorldChild(final String name) {
        this.name = name;
    }



    private String name;

    @Title(prepend = "Child: ")
    @Name
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.IDENTITY, sequence = "1")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    private String notes;
    private boolean enabled = true;

    public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Notes
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "1")
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }



    @Action(
            semantics = SemanticsOf.IDEMPOTENT,
            executionPublishing = Publishing.ENABLED
    )
    @ActionLayout(
            associateWith = "name",
            describedAs = "Updates the object's name"
    )
    public HelloWorldChild updateName(
            @Name final String name) {
        setName(name);
        return this;
    }
    public String default0UpdateName() {
        return getName();
    }



    @Action(
            semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE
    )
    @ActionLayout(
            fieldSetId = LayoutConstants.FieldSetId.IDENTITY,
            describedAs = "Deletes this object from the database",
            position = ActionLayout.Position.PANEL
    )
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.removeAndFlush(this);
    }



    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(final HelloWorldChild other) {
        return Comparator.comparing(HelloWorldChild::getName).compare(this, other);
    }


    @Inject RepositoryService repositoryService;
    @Inject TitleService titleService;
    @Inject MessageService messageService;

}
