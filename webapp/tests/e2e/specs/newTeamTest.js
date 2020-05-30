//useful information
//after clicking SEND REQUEST it does as follows:
//alert : {response.status}
//alert : *massage matching the response status
describe('New Team Page Test', () => {
    it('test the navigation to New Team Page (unlogged)', () => {
        cy.visit("localhost:8080")//enter the site on the localhost
        cy.get('button').eq(0).click()//open menu
        cy.contains('div', 'My Account').click({force:true})//click on My Account menu
        cy.contains('div', 'New team').click({force:true})//click on New Team button
        cy.contains('h1','Please Log in to continue')//check the message on page
    })
  })
describe('New Team Page Test', () => {
    it('sends a new team request', () => {
        cy.visit("localhost:8080")//enter the site on localhost
        //login
        cy.contains('span', 'Login').click()//click on login button
        cy.get('input').eq(1).click({force:true}).type('representative')//insert user name
        cy.get('input').eq(2).click({force:true}).type('Password123')//insert password
        cy.get('button').contains('Login').eq(0).click()//click on login
        cy.wait(3000)//wait for response
        //check the alert massage
        cy.on('window:alert', (str) => {
            expect(str).to.contains(`Logged In successfully `)
          })//on
        //navigate to new team form
        cy.get('button').eq(0).click()//open menu
        cy.contains('div', 'My Account').click({force:true})//click on My Account menu
        cy.contains('div', 'New team').click({force:true})//click on New Team
        cy.get('input').eq(1).click({force:true}).type('new team')//insert team name in text-field
        cy.contains('span', 'Send Request').click({force:true})//click send
        cy.contains('span','All-User Logout').click();//logout
        //sending new team request..18 realteam
        //404
    })
  })