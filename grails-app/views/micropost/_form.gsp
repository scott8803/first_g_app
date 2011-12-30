<%@ page import="first_g_app.Micropost" %>



<div class="fieldcontain ${hasErrors(bean: micropostInstance, field: 'content', 'error')} ">
	<label for="content">
		<g:message code="micropost.content.label" default="Content" />
		
	</label>
	<g:textField name="content" value="${micropostInstance?.content}"/>
</div>

