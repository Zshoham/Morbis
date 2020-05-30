describe('Logout Test', () => {
    it('logs in and then logs out', () => {
      cy.visit("localhost:8080")
      cy.contains('span', 'Login').click()//click on Login button
      cy.get('input').eq(1).click({force:true}).type('representative')//insert username 
      cy.get('input').eq(2).click({force:true}).type('Password123')//insert password
      cy.get('button').contains('Login').eq(0).click()//click on login button
      cy.wait(3000)//wait for response
      cy.url().should('include','HomePage')//check if the redirection is correct (to homepage)
      cy.contains('span', 'Logout').click()//click on logout
      cy.wait(3000)//wait of response
      cy.url().should('include','WelcomePage')//check if the redirection is correct (to welcomepage)
      })//it
    })//describe