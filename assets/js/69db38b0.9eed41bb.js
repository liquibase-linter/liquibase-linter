"use strict";(self.webpackChunk=self.webpackChunk||[]).push([[9641],{3331:(e,n,t)=>{t.r(n),t.d(n,{assets:()=>i,contentTitle:()=>o,default:()=>d,frontMatter:()=>l,metadata:()=>s,toc:()=>c});const s=JSON.parse('{"id":"rules/column-name","title":"column-name","description":"Why?","source":"@site/docs/rules/column-name.md","sourceDirName":"rules","slug":"/rules/column-name","permalink":"/liquibase-linter/docs/rules/column-name","draft":false,"unlisted":false,"tags":[],"version":"current","frontMatter":{"title":"column-name"},"sidebar":"docs","previous":{"title":"changeset-id","permalink":"/liquibase-linter/docs/rules/changeset-id"},"next":{"title":"column-type","permalink":"/liquibase-linter/docs/rules/column-type"}}');var a=t(4848),r=t(8453);const l={title:"column-name"},o=void 0,i={},c=[{value:"Why?",id:"why",level:2},{value:"Options",id:"options",level:2},{value:"Example Usage",id:"example-usage",level:2}];function u(e){const n={a:"a",code:"code",h2:"h2",li:"li",p:"p",pre:"pre",ul:"ul",...(0,r.R)(),...e.components};return(0,a.jsxs)(a.Fragment,{children:[(0,a.jsx)(n.h2,{id:"why",children:"Why?"}),"\n",(0,a.jsxs)(n.p,{children:["You might already have a broad standard for object names - and be enforcing it with ",(0,a.jsx)(n.a,{href:"/liquibase-linter/docs/rules/object-name",children:"the object-name rule"})," - but you might also want a more specific rule concerning how columns are named."]}),"\n",(0,a.jsx)(n.p,{children:"This rule will fail if the given regex does not match against the name when creating or renaming a column."}),"\n",(0,a.jsx)(n.h2,{id:"options",children:"Options"}),"\n",(0,a.jsxs)(n.ul,{children:["\n",(0,a.jsxs)(n.li,{children:[(0,a.jsx)(n.code,{children:"pattern"})," - (regex, as string) regular expression that the name of any created or renamed column must adhere to"]}),"\n"]}),"\n",(0,a.jsx)(n.h2,{id:"example-usage",children:"Example Usage"}),"\n",(0,a.jsx)(n.pre,{children:(0,a.jsx)(n.code,{className:"language-json",children:'{\n  "rules": {\n    "column-name": {\n      "pattern": "^[a-z_]+$",\n      "errorMessage": "Column name \'%s\' should be lower cased"\n    }\n  }\n}\n'})}),"\n",(0,a.jsx)(n.p,{children:"(The above example just ensures that columns should have a lowercased name.)"})]})}function d(e={}){const{wrapper:n}={...(0,r.R)(),...e.components};return n?(0,a.jsx)(n,{...e,children:(0,a.jsx)(u,{...e})}):u(e)}},8453:(e,n,t)=>{t.d(n,{R:()=>l,x:()=>o});var s=t(6540);const a={},r=s.createContext(a);function l(e){const n=s.useContext(r);return s.useMemo((function(){return"function"==typeof e?e(n):{...n,...e}}),[n,e])}function o(e){let n;return n=e.disableParentContext?"function"==typeof e.components?e.components(a):e.components||a:l(e.components),s.createElement(r.Provider,{value:n},e.children)}}}]);