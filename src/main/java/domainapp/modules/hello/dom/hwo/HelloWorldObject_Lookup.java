package domainapp.modules.hello.dom.hwo;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Parameter;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;

@Action
@RequiredArgsConstructor
public class HelloWorldObject_Lookup {
	private final HelloWorldObject target;

	@Value @Accessors(fluent = true)
	static class Parameters {
		String objectName;
		String childName;
	}

	public HelloWorldChild act(
			@Parameter String objectName,
			@Parameter String childName) {
		return helloWorldObjects.findByName(objectName)
				.get(0)
				.getChildren()
				.stream()
				.filter((o)->o.getName().equals(childName))
				.findFirst()
				.get();
	}
	
	public Collection<String> choicesObjectName(Parameters params) {
		return helloWorldObjects
				.listAll()
				.stream()
				.map((o)->o.getName())
				.collect(Collectors.toList())
				;
	}
	
	public Collection<String> autoCompleteChildName(Parameters params,
			@MinLength(3) String search) {
		return helloWorldObjects.findByName(params.objectName)
		.get(0)
		.getChildren()
		.stream()
		.map((o)->o.getName())
		.collect(Collectors.toList());
	}
	
	
	@Inject
	HelloWorldObjects helloWorldObjects;
	
	
}
