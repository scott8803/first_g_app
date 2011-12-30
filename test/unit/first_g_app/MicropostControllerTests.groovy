package first_g_app



import org.junit.*
import grails.test.mixin.*

@TestFor(MicropostController)
@Mock(Micropost)
class MicropostControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/micropost/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.micropostInstanceList.size() == 0
        assert model.micropostInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.micropostInstance != null
    }

    void testSave() {
        controller.save()

        assert model.micropostInstance != null
        assert view == '/micropost/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/micropost/show/1'
        assert controller.flash.message != null
        assert Micropost.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/micropost/list'


        populateValidParams(params)
        def micropost = new Micropost(params)

        assert micropost.save() != null

        params.id = micropost.id

        def model = controller.show()

        assert model.micropostInstance == micropost
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/micropost/list'


        populateValidParams(params)
        def micropost = new Micropost(params)

        assert micropost.save() != null

        params.id = micropost.id

        def model = controller.edit()

        assert model.micropostInstance == micropost
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/micropost/list'

        response.reset()


        populateValidParams(params)
        def micropost = new Micropost(params)

        assert micropost.save() != null

        // test invalid parameters in update
        params.id = micropost.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/micropost/edit"
        assert model.micropostInstance != null

        micropost.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/micropost/show/$micropost.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        micropost.clearErrors()

        populateValidParams(params)
        params.id = micropost.id
        params.version = -1
        controller.update()

        assert view == "/micropost/edit"
        assert model.micropostInstance != null
        assert model.micropostInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/micropost/list'

        response.reset()

        populateValidParams(params)
        def micropost = new Micropost(params)

        assert micropost.save() != null
        assert Micropost.count() == 1

        params.id = micropost.id

        controller.delete()

        assert Micropost.count() == 0
        assert Micropost.get(micropost.id) == null
        assert response.redirectedUrl == '/micropost/list'
    }
}
