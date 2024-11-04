"use strict";(self.webpackChunk=self.webpackChunk||[]).push([[587],{573:(e,t,n)=>{n.r(t),n.d(t,{assets:()=>a,contentTitle:()=>s,default:()=>d,frontMatter:()=>i,metadata:()=>l,toc:()=>c});const l=JSON.parse('{"id":"rules/create-column-nullable-constraint","title":"create-column-nullable-constraint","description":"Why?","source":"@site/docs/rules/create-column-nullable-constraint.md","sourceDirName":"rules","slug":"/rules/create-column-nullable-constraint","permalink":"/liquibase-linter/docs/rules/create-column-nullable-constraint","draft":false,"unlisted":false,"tags":[],"version":"current","frontMatter":{"title":"create-column-nullable-constraint"},"sidebar":"docs","previous":{"title":"create-column-no-define-primary-key","permalink":"/liquibase-linter/docs/rules/create-column-no-define-primary-key"},"next":{"title":"create-column-remarks","permalink":"/liquibase-linter/docs/rules/create-column-remarks"}}');var o=n(4848),r=n(8453);const i={title:"create-column-nullable-constraint"},s=void 0,a={},c=[{value:"Why?",id:"why",level:2},{value:"Options",id:"options",level:2}];function u(e){const t={a:"a",code:"code",h2:"h2",p:"p",...(0,r.R)(),...e.components};return(0,o.jsxs)(o.Fragment,{children:[(0,o.jsx)(t.h2,{id:"why",children:"Why?"}),"\n",(0,o.jsxs)(t.p,{children:["When defining a ",(0,o.jsx)(t.a,{href:"https://www.liquibase.org/documentation/column.html",children:"column"}),", you are free to omit the ",(0,o.jsx)(t.code,{children:"nullable"})," attribute on the ",(0,o.jsx)(t.code,{children:"<constraints>"})," tag (or just omit the tag altogether), and the implicit default behaviour is that the column is added as nullable. This is fine and works consistently across vendors, but you might want to make the developer have to explicitly state whether the column is nullanble or not, to ensure they have not overlooked this detail in their design."]}),"\n",(0,o.jsxs)(t.p,{children:["This rule will fail if a new column is specified without a ",(0,o.jsx)(t.code,{children:"<constraints>"})," tag with a ",(0,o.jsx)(t.code,{children:"nullable"})," attribute."]}),"\n",(0,o.jsx)(t.h2,{id:"options",children:"Options"}),"\n",(0,o.jsx)(t.p,{children:"No extra options."})]})}function d(e={}){const{wrapper:t}={...(0,r.R)(),...e.components};return t?(0,o.jsx)(t,{...e,children:(0,o.jsx)(u,{...e})}):u(e)}},8453:(e,t,n)=>{n.d(t,{R:()=>i,x:()=>s});var l=n(6540);const o={},r=l.createContext(o);function i(e){const t=l.useContext(r);return l.useMemo((function(){return"function"==typeof e?e(t):{...t,...e}}),[t,e])}function s(e){let t;return t=e.disableParentContext?"function"==typeof e.components?e.components(o):e.components||o:i(e.components),l.createElement(r.Provider,{value:t},e.children)}}}]);