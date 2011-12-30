package first_g_app

import org.springframework.dao.DataIntegrityViolationException

class MicropostController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [micropostInstanceList: Micropost.list(params), micropostInstanceTotal: Micropost.count()]
    }

    def create() {
        [micropostInstance: new Micropost(params)]
    }

    def save() {
        def micropostInstance = new Micropost(params)
        if (!micropostInstance.save(flush: true)) {
            render(view: "create", model: [micropostInstance: micropostInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'micropost.label', default: 'Micropost'), micropostInstance.id])
        redirect(action: "show", id: micropostInstance.id)
    }

    def show() {
        def micropostInstance = Micropost.get(params.id)
        if (!micropostInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'micropost.label', default: 'Micropost'), params.id])
            redirect(action: "list")
            return
        }

        [micropostInstance: micropostInstance]
    }

    def edit() {
        def micropostInstance = Micropost.get(params.id)
        if (!micropostInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'micropost.label', default: 'Micropost'), params.id])
            redirect(action: "list")
            return
        }

        [micropostInstance: micropostInstance]
    }

    def update() {
        def micropostInstance = Micropost.get(params.id)
        if (!micropostInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'micropost.label', default: 'Micropost'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (micropostInstance.version > version) {
                micropostInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'micropost.label', default: 'Micropost')] as Object[],
                          "Another user has updated this Micropost while you were editing")
                render(view: "edit", model: [micropostInstance: micropostInstance])
                return
            }
        }

        micropostInstance.properties = params

        if (!micropostInstance.save(flush: true)) {
            render(view: "edit", model: [micropostInstance: micropostInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'micropost.label', default: 'Micropost'), micropostInstance.id])
        redirect(action: "show", id: micropostInstance.id)
    }

    def delete() {
        def micropostInstance = Micropost.get(params.id)
        if (!micropostInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'micropost.label', default: 'Micropost'), params.id])
            redirect(action: "list")
            return
        }

        try {
            micropostInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'micropost.label', default: 'Micropost'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'micropost.label', default: 'Micropost'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
