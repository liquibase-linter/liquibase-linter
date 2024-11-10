"use strict";(self.webpackChunk=self.webpackChunk||[]).push([[1655],{9874:(e,n,t)=>{t.r(n),t.d(n,{assets:()=>l,contentTitle:()=>s,default:()=>d,frontMatter:()=>o,metadata:()=>r,toc:()=>c});const r=JSON.parse('{"id":"rules/create-column-no-define-primary-key","title":"create-column-no-define-primary-key","description":"Why?","source":"@site/docs/rules/create-column-no-define-primary-key.md","sourceDirName":"rules","slug":"/rules/create-column-no-define-primary-key","permalink":"/liquibase-linter/docs/rules/create-column-no-define-primary-key","draft":false,"unlisted":false,"tags":[],"version":"current","frontMatter":{"title":"create-column-no-define-primary-key"},"sidebar":"docs","previous":{"title":"Using Rules","permalink":"/liquibase-linter/docs/rules/"},"next":{"title":"create-column-nullable-constraint","permalink":"/liquibase-linter/docs/rules/create-column-nullable-constraint"}}');var i=t(4848),a=t(8453);const o={title:"create-column-no-define-primary-key"},s=void 0,l={},c=[{value:"Why?",id:"why",level:2},{value:"Options",id:"options",level:2}];function u(e){const n={a:"a",code:"code",h2:"h2",p:"p",pre:"pre",...(0,a.R)(),...e.components};return(0,i.jsxs)(i.Fragment,{children:[(0,i.jsx)(n.h2,{id:"why",children:"Why?"}),"\n",(0,i.jsxs)(n.p,{children:["When defining a ",(0,i.jsx)(n.a,{href:"https://www.liquibase.org/documentation/column.html",children:"column"}),", it's possible to indicate that it's the primary key, like this:"]}),"\n",(0,i.jsx)(n.pre,{children:(0,i.jsx)(n.code,{className:"language-xml",children:'<addColumn tableName="FOO">\n    <column name="BAR" type="${nvarchar}(50)">\n        <constraints primaryKey="true"/>    \n    </column>\n</addColumn>\n'})}),"\n",(0,i.jsxs)(n.p,{children:["This is convenient but can be hazardous, because if there's a problem with the creation of the constraint, you are left with a part-done change for that database and will need to manually roll it back. You might prefer to create the primary key constraint in a ",(0,i.jsx)(n.a,{href:"http://www.liquibase.org/documentation/changes/add_primary_key.html",children:"separate change"}),"."]}),"\n",(0,i.jsxs)(n.p,{children:["This rule will fail if a ",(0,i.jsx)(n.code,{children:"primaryKey"})," attribute is found on a ",(0,i.jsx)(n.code,{children:"<constraints>"})," tag when adding a column."]}),"\n",(0,i.jsx)(n.h2,{id:"options",children:"Options"}),"\n",(0,i.jsx)(n.p,{children:"No extra options."})]})}function d(e={}){const{wrapper:n}={...(0,a.R)(),...e.components};return n?(0,i.jsx)(n,{...e,children:(0,i.jsx)(u,{...e})}):u(e)}},8453:(e,n,t)=>{t.d(n,{R:()=>o,x:()=>s});var r=t(6540);const i={},a=r.createContext(i);function o(e){const n=r.useContext(a);return r.useMemo((function(){return"function"==typeof e?e(n):{...n,...e}}),[n,e])}function s(e){let n;return n=e.disableParentContext?"function"==typeof e.components?e.components(i):e.components||i:o(e.components),r.createElement(a.Provider,{value:n},e.children)}}}]);