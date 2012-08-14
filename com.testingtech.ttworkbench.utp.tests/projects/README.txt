test resource projects

these folders are valid ttcn-3 projects. they will be zipped by "customBuildCallbacks.xml"
into the folder "archives". each archive must be registered in plugin.xml. contribute it as
a "project" subitem of a "projectpacket" item. name of project must be equal to original
folder name here in "projects". the projectpacket must be tagged (category) as "test".

in the junit plugin test these "test" project packets will be expanded into the workspace
and the tests will use them. folder and file names in the tests must be valid. during tests
for each test (input) project a corresponding output is created.
